FROM ghcr.io/graalvm/native-image-community:22-muslib as build-image

WORKDIR /work
COPY ./ ./

RUN microdnf install findutils

RUN ./gradlew clean nativeCompile
RUN chmod +x ./build/native/nativeCompile/lambda-kotlin-sls

FROM public.ecr.aws/lambda/provided:al2

COPY --from=build-image /work/build/native/nativeCompile/lambda-kotlin-sls /var/runtime/bootstrap

CMD ["dummyHandler"]
