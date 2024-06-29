#!/bin/sh

aws configure set aws_access_key_id dummy --profile local
aws configure set aws_secret_access_key dummy --profile local
aws configure set region us-east-1 --profile local
