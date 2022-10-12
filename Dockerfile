FROM ghcr.io/graalvm/native-image:latest as build-image

ARG BUILD_DIR=/build-lib
ARG WORK_DIR=/work-lib
ARG MSUL_VERSION=1.2.3
ARG ZLIB_VERSION=1.2.12

RUN microdnf install yum
ENV LC_ALL C
RUN yum -y install wget
RUN yum -y groupinstall "Development Tools"
RUN yum -y install kernel-devel kernel-headers
RUN mkdir ${BUILD_DIR}
RUN mkdir ${WORK_DIR}
ENV PATH $PATH:${BUILD_DIR}/bin

WORKDIR ${WORK_DIR}
RUN wget http://www.musl-libc.org/releases/musl-${MSUL_VERSION}.tar.gz
RUN tar xzvf musl-${MSUL_VERSION}.tar.gz
WORKDIR ${WORK_DIR}/musl-${MSUL_VERSION}
RUN ./configure --disable-shared --prefix=${BUILD_DIR}
RUN make
RUN make install

WORKDIR ${WORK_DIR}
RUN wget http://zlib.net/zlib-${ZLIB_VERSION}.tar.gz
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
