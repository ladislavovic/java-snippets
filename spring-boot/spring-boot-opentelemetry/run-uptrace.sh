#!/bin/bash

#
# This file runs the application with opentelemetry and export data to Uptrace. For more information
# about opentelemetry itself look at the run.sh script.
#
# Uptrace is a cloud service for application monitoring. I've registered there and get a DSN, which is
# a secret I can authorize there. Some links:
#
# The dashboard: https://app.uptrace.dev/overview/6307/419
# Documentation: https://uptrace.dev/get/opentelemetry-java

# Everything in this variable is automatically prepended to the JVM command line every time a Java application is launched
# It is a way how to set up JVM options globally
export JAVA_TOOL_OPTIONS="-javaagent:opentelemetry-javaagent-2-15-0.jar"

#
# OpenTelemetry configuration for Uptrace
#
export UPTRACE_DSN="https://bjcN8Mj2t1p8Xr0RqPF9EA@api.uptrace.dev?grpc=4317"

# Defines resource attributes that describe the service sending telemetry data.
export OTEL_RESOURCE_ATTRIBUTES=service.name=myservice,service.version=1.0.0

# Export data as OTLP
export OTEL_TRACES_EXPORTER=otlp
export OTEL_METRICS_EXPORTER=otlp
export OTEL_LOGS_EXPORTER=otlp

# The compression to use on OTLP trace, no copression by defaul
export OTEL_EXPORTER_OTLP_COMPRESSION=gzip

# Endpoint where to send OTLP data
export OTEL_EXPORTER_OTLP_ENDPOINT=https://api.uptrace.dev:4317

# Adds custom headers to the OTLP request.
export OTEL_EXPORTER_OTLP_HEADERS="uptrace-dsn=${UPTRACE_DSN}"

# Delta or cumulative
export OTEL_EXPORTER_OTLP_METRICS_TEMPORALITY_PREFERENCE=DELTA

export OTEL_EXPORTER_OTLP_METRICS_DEFAULT_HISTOGRAM_AGGREGATION=BASE2_EXPONENTIAL_BUCKET_HISTOGRAM
export OTEL_METRIC_EXPORT_INTERVAL=15000

# This was not in the documentation, but without that id did not work, probably
# The default protocol is Protobuf
export OTEL_EXPORTER_OTLP_PROTOCOL=grpc

java -jar ./target/spring-boot-opentelemetry-0.0.1-SNAPSHOT.jar
