FROM ghcr.io/graalvm/graalvm-ce:latest as build-image

#RUN yum install unzip
#RUN curl -s "https://get.sdkman.io" | bash
#RUN source "$HOME/.sdkman/bin/sdkman-init.sh"
#RUN sdk install kotlin
#RUN gu install kotlin

WORKDIR /work
COPY ./ ./

RUN ./gradlew nativeImage
#RUN mv ./target/native-image/bootstrap .
#RUN chmod +x bootstrap
#
#FROM public.ecr.aws/lambda/provided:al2
#
#COPY --from=build-image /work/bootstrap /var/runtime/
#
#CMD ["dummyHandler"]

CMD ["kotlin", "-version"]
