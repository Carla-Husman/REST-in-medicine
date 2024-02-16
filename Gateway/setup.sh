#!/bin/bash

echo "Installing dependencies..."

sudo pip install grpcio grpcio-tools
python3 -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. authentication.proto