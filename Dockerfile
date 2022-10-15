FROM ghcr.io/graalvm/graalvm-ce:ol8-java11 as build-image

RUN gu install native-image

ARG CC_DIR=/cc-lib
ARG BUILD_DIR=/build-lib
ARG WORK_DIR=/work-lib
ARG CC_VERSION=11.2.1
ARG MSUL_VERSION=1.2.3
ARG ZLIB_VERSION=1.2.13

RUN microdnf install yum
ENV LC_ALL C
RUN yum -y install wget
RUN mkdir ${CC_DIR}
RUN mkdir ${BUILD_DIR}
RUN mkdir ${WORK_DIR}
ENV PATH $PATH:${BUILD_DIR}/bin

WORKDIR ${CC_DIR}
ENV CC ${CC_DIR}/x86_64-linux-musl-native/bin/gcc
ENV PATH $PATH:${CC_DIR}/x86_64-linux-musl-native/bin
RUN wget https://more.musl.cc/${CC_VERSION}/x86_64-linux-musl/x86_64-linux-musl-native.tgz
RUN tar -xvzf x86_64-linux-musl-native.tgz

WORKDIR ${WORK_DIR}
RUN wget http://www.musl-libc.org/releases/musl-${MSUL_VERSION}.tar.gz
RUN tar xzvf musl-${MSUL_VERSION}.tar.gz
WORKDIR ${WORK_DIR}/musl-${MSUL_VERSION}
RUN ./configure --disable-shared --prefix=${BUILD_DIR}
RUN make
RUN make install

WORKDIR ${WORK_DIR}
RUN wget https://zlib.net/zlib-${ZLIB_VERSION}.tar.gz
RUN tar xzvf zlib-${ZLIB_VERSION}.tar.gz
WORKDIR ${WORK_DIR}/zlib-${ZLIB_VERSION}
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
