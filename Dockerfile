# =====================================================================
# Docker(ECR イメージ)版の Dockerfile【旧構成・参考用にコメントアウト】
# ---------------------------------------------------------------------
# zip(provided.al2)版へ移行したため未使用。
# zip 版では serverless.yml の scripts フックが
# ghcr.io/graalvm/native-image-community:22-muslib イメージで
# `./gradlew clean nativeCompile` を実行し、生成物を bootstrap に
# リネームするため、この Dockerfile は不要になった。
# ---------------------------------------------------------------------
# FROM ghcr.io/graalvm/native-image-community:22-muslib as build-image
#
# WORKDIR /work
# COPY ./ ./
#
# RUN microdnf install findutils
#
# RUN ./gradlew clean nativeCompile
# RUN chmod +x ./build/native/nativeCompile/lambda-kotlin-sls
#
# FROM public.ecr.aws/lambda/provided:al2
#
# COPY --from=build-image /work/build/native/nativeCompile/lambda-kotlin-sls /var/runtime/bootstrap
#
# CMD ["dummyHandler"]
# =====================================================================
