FROM gradle:latest as build-image

RUN apt-get update -y
RUN apt-get install -y libtinfo5

WORKDIR /work
COPY ./ ./

RUN ./gradlew build
RUN mv build/bin/native/releaseExecutable/lambda-kotlin-sls.kexe bootstrap
RUN chmod +x bootstrap

FROM public.ecr.aws/lambda/provided:al2

COPY --from=build-image /work/ /var/runtime/

CMD ["hello"]