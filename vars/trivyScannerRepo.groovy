def call(Map config = [:]) {
    sh """ #!/bin/bash
    output_path=${config.outputPath}
    report_name=$(basename $(pwd))
    version=\$(date +'%y.%m.%d')
    report_name=\$BUILD_ID-/$reportName-\$version.txt
    result=\$(trivy fs . -o $report_name )

    echo \$result
    ls -altr \$output_path
    """
}
