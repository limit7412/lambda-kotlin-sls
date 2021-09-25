FROM findepi/graalvm:java11-native as build-image

ENV JAVA_HOME /graalvm
RUN gu install native-image
RUN apt-get -y update
RUN apt-get -y install musl
RUN apt-get -y install musl-tools

WORKDIR /work
COPY ./ ./

RUN ./gradlew nativeImage
RUN chmod +x ./build/executable/bootstrap

FROM public.ecr.aws/lambda/provided:al2

COPY --from=build-image /work/build/executable/bootstrap /var/runtime/

CMD ["dummyHandler"]
