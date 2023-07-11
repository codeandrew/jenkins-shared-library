def call(Map config = [:]) {
    sh """ #!/bin/bash
    set -x
    output_path=${config.outputPath}
    repo_name=\$(basename \$(pwd))
    version=\$(date +'%y.%m.%d')
    REPORT_NAME=\$BUILD_ID-\$repo_name-\$version

    pwd; ls -latr
    printenv | sort
    mkdir -p \$output_path

    
    if [ -f "trivy-repo-summary.json" ]; then
        rm trivy-repo-summary.json
    fi
    
    trivy fs . --no-progress -f json -o trivy-repo-summary.json
    result=\$(trivy fs . -o \$output_path/\$REPORT_NAME.txt )
    echo \$result
    ls -altr \$output_path
    """
}
