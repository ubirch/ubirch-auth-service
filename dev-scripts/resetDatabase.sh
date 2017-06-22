#!/usr/bin/env bash
cd ..

echo "======"
echo "====== STEP 1/2: start to delete database"
echo "======"
./sbt "cmdtools/runMain com.ubirch.auth.cmd.RedisDelete"
echo "======"
echo "====== STEP 1/2: finished deleting database"
echo "======"

echo "======"
echo "====== STEP 2/2: start to initialize default OpenID Connect providers"
echo "======"
./sbt "cmdtools/runMain com.ubirch.auth.cmd.InitData"
echo "======"
echo "====== STEP 2/2: finished initializing default OpenID Connect providers"
echo "======"
