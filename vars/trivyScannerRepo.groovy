def call(Map config = [:]) {
    sh """ #!/bin/bash
    set -x
    output_path=${config.outputPath}
    echo \$output_path
    
    repo_name=\$(basename \$(pwd))
    
    """
}

    
    
    // version=\$(date +'%y.%m.%d')
    // REPORT_NAME=\$BUILD_ID-\$repo_name-\$version.txt
    
    // mkdir -p \$OUTPUT_DIR
    // result=\$(trivy fs . -o $OUTPUT_DIR/\$REPORT_NAME )

    // echo \$result
    // ls -altr \$OUTPUT_DIR