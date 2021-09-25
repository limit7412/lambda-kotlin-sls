FROM findepi/graalvm:java11-native as build-image

RUN apt-get -y install musl-tools
RUN gu install native-image
ENV JAVA_HOME /graalvm

WORKDIR /work
COPY ./ ./

RUN ./gradlew nativeImage
RUN chmod +x ./build/executable/bootstrap

FROM public.ecr.aws/lambda/provided:al2

COPY --from=build-image /work/build/executable/bootstrap /var/runtime/

CMD ["dummyHandler"]
