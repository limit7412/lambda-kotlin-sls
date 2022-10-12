FROM ghcr.io/graalvm/native-image:latest as build-image

ENV JAVA_HOME /graalvm
RUN gu install native-image

ARG BUILD_DIR=/build-lib
ARG WORK_DIR=/work-lib
RUN apt-get -y install wget
RUN apt-get -y install build-essential
RUN mkdir ${BUILD_DIR}
RUN mkdir ${WORK_DIR}
ENV PATH $PATH:${BUILD_DIR}/bin

WORKDIR ${WORK_DIR}
RUN wget http://www.musl-libc.org/releases/musl-1.2.0.tar.gz
RUN tar xzvf musl-1.2.0.tar.gz
WORKDIR ${WORK_DIR}/musl-1.2.0
RUN ./configure --disable-shared --prefix=${BUILD_DIR}
RUN make
RUN make install

WORKDIR ${WORK_DIR}
RUN wget http://zlib.net/zlib-1.2.11.tar.gz
RUN tar xzvf zlib-1.2.11.tar.gz
WORKDIR ${WORK_DIR}/zlib-1.2.11
RUN ./configure --static --prefix=${BUILD_DIR}
RUN make
RUN make install

WORKDIR /work
COPY ./ ./

RUN ./gradlew nativeImage
RUN chmod +x ./build/executable/bootstrap

FROM public.ecr.aws/lambda/provided:al2

COPY --from=build-image /work/build/executable/bootstrap /var/runtime/

CMD ["dummyHandler"]
