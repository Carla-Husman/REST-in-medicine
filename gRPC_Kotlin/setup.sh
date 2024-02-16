#!/bin/bash

echo "Installing grpc and protobuf"

PROTOC="Server/src/main/resources/protoc-25.1-win64/bin/"
PROTOC_GEN_GRPC_JAVA="Server/src/main/resources/protoc-gen-grpc-java-1.60.0-windows-x86_64.exe"
DIR_OF_RESOURCES="Server/src/main/resources"
PROTO_FILE="authentication.proto"
OUTPUT_FILE="Server/src/main/kotlin"
protoc --proto_path="$PROTOC" --java_out="$OUTPUT_FILE" --proto_path="$DIR_OF_RESOURCES" "$PROTO_FILE"
protoc --plugin=protoc-gen-grpc-java="$PROTOC_GEN_GRPC_JAVA" --grpc-java_out="$OUTPUT_FILE" --proto_path="$DIR_OF_RESOURCES" "$PROTO_FILE"

