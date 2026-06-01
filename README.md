# lambda-kotlin-sls

自力でシングルバイナリなサーバーレスkotlin作ってみたやつ by Graalvm

要: Docker, Node.js, Serverless Framework

zip(provided.al2)版で動作する。`sls deploy` 時に serverless-plugin-scripts の
フックが GraalVM の Docker イメージで `nativeCompile` を実行し、生成された
ネイティブバイナリを `bootstrap` にリネームして zip にパッケージングする。

```bash
# プラグインのインストール（初回のみ）
$ npm install

# deploy
$ sls deploy --stage <stage_name>

# remove
$ sls remove --stage <stage_name>
```

> 旧構成の Docker(ECR イメージ)版は `serverless.yml` / `Dockerfile` に
> コメントアウトで残してある。
