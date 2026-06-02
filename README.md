# lambda-kotlin-sls

自力でシングルバイナリなサーバーレスkotlin作ってみたやつ by Kotlin/Native

要: Docker, Node.js, Serverless Framework

zip(provided.al2023)版で動作する。`sls deploy` 時に serverless-plugin-scripts の
フックが JDK 入りの Docker イメージで Kotlin/Native の
`linkReleaseExecutableLinuxX64` を実行し、生成された実行ファイル
`bootstrap.kexe` を `bootstrap` にリネームして zip にパッケージングする。

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
