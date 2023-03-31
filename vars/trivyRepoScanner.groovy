def call(Map config = [:]) {
    sh """ #!/bin/bash
    set -x
    output_path=${config.outputPath}
    repo_name=\$(basename \$(pwd))
    version=\$(date +'%y.%m.%d')
    REPORT_NAME=\$BUILD_ID-\$repo_name-\$version.txt

    pwd; ls -latr
    printenv | sort
    mkdir -p \$output_path

    result=\$(trivy fs . -o \$output_path/\$REPORT_NAME )
    echo \$result
    ls -altr \$output_path
    """
}