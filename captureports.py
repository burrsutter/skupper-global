from __future__ import print_function
import sys
import os
import platform
import json

try:
    filename = sys.argv[1]
    is_file = os.path.isfile(filename)
    if not is_file:
        raise Exception()
except Exception as e:
    print ("Usage: python3 captureports.py <absolute_file_path>. Example - python3 captureports.py /tmp/qdrouterd.json")
    ## Unix programs generally use 2 for command line syntax errors
    sys.exit(2)

ports=['5671','5672','55672']

with open(filename) as read_file:
    data = json.load(read_file)
    for item in data:
        for config in item:
            if isinstance(config, str) and config.endswith('Listener'):
                ports.append(item[1]['port'])

if platform.system() != 'Linux':
    mapped_ports = ''
    for port in ports:
        mapped_ports += '-p {0}:{0} '.format(port)
    print(mapped_ports)