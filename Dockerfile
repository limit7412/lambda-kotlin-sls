FROM gradle:7.5.1-jdk17 as build-image

RUN ./gradlew nativeBinaries
RUN chmod +x ./build/executable/bootstrap

FROM public.ecr.aws/lambda/provided:al2

COPY --from=build-image /work/build/releaseExecutable/bootstrap /var/runtime/

CMD ["dummyHandler"]
