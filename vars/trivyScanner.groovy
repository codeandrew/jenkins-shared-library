def call(Map config = [:]) {
    git credentialsId: "${config.gitSecrets}", url: 'https://github.com/codeandrew/scanner-service.git'
    branch: 'main'
    sh """ #!/bin/bash
    cd client

    target_image=${config.targetImage}
    output_path=${config.outputPath}
    version=\$(date +'%y.%m.%d')
    report_name=\$BUILD_ID-\$(echo \$target_image | sed 's/:/_/')-\$version.json
    

    docker pull \$target_image
    result=\$(bash trivy-executor.sh \
        --image \$target_image \
        --output-path \$output_path\
        --report-name \$report_name)

    echo \$result
    ls -altr \$output_path
    SCANNER_URL=${config.scannerURL} python3 scanner-client.py \$result
    """
}