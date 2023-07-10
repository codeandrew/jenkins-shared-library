def call(Map config = [:]) {
    def reportName = "${config.targetImage}".replaceAll("/", "_").replaceAll(":", "_")

    sh """ #!/bin/bash
    set -x
    target_image=${config.targetImage}
    output_path=${config.outputPath}
    version=\$(date +'%y.%m.%d')

    mkdir -p \$output_path
    report_name=\$BUILD_ID-${reportName}-\$version

    RESULT_PATH=\$output_path/\$report_name
    docker pull \$target_image
    trivy image  --no-progress -f json -o \$RESULT_PATH.json $target_image    
    result=\$(trivy image  -o \$RESULT_PATH.txt \$target_image )

    echo \$result
    ls -altr \$output_path
    """
}
