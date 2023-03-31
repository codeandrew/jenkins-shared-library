def call(Map config = [:]) {
    def reportName = "${config.targetImage}".replaceAll("/", "_").replaceAll(":", "_")

    sh """ #!/bin/bash
    set -x
    target_image=${config.targetImage}
    output_path=${config.outputPath}
    version=\$(date +'%y.%m.%d')

    mkdir -p \$output_path
    report_name=\$BUILD_ID-${reportName}-\$version.txt

    RESULT=\$output_path/\$report_name
    docker pull \$target_image
    result=\$(trivy image  -o $output_path/\$report_name )

    echo \$result
    ls -altr \$output_path
    """
}
