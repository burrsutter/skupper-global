elif [ "$type" == "docker" ] || [ "$type" == "podman" ]; then
    if [[ "$OSTYPE" == "linux"* ]]; then
        network_config="--network host"
    else
        network_config="--hostname ${gateway_name} $(python3 ./captureports.py config/skrouterd.json)"
    fi
#    ${type} run --restart always -d \
    ${type} run --restart always -it \
       --name ${gateway_name} ${network_config} \
       -e QDROUTERD_CONF_TYPE=json \
       -e QDROUTERD_CONF=/opt/skupper/config/skrouterd.json \
       -v ${local_dir}:${QDR_CONF_DIR}:Z \
       ${gateway_image}
    exit    
fi