FROM findepi/graalvm:java11-native as build-image

ENV JAVA_HOME /graalvm
RUN gu install native-image

RUN mkdir /build-lib
RUN mkdir /work-lib

WORKDIR /build-lib
RUN wget http://www.musl-libc.org/releases/musl-1.2.0.tar.gz
RUN tar xzvf musl-1.2.0.tar.gz
WORKDIR /build-lib/musl-1.2.0
RUN ./configure --disable-shared --prefix=/work-lib
RUN make
RUN make install

WORKDIR /build-lib
RUN wget http://zlib.net/zlib-1.2.11.tar.gz
RUN tar xzvf zlib-1.2.11.tar.gz
WORKDIR /build-lib/zlib-1.2.11
RUN ./configure --static --prefix=/work-lib
RUN make
RUN make install

WORKDIR /work
COPY ./ ./

RUN ./gradlew nativeImage
RUN chmod +x ./build/executable/bootstrap

FROM public.ecr.aws/lambda/provided:al2

COPY --from=build-image /work/build/executable/bootstrap /var/runtime/

CMD ["dummyHandler"]
