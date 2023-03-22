def call(Map config = [:]) {
    sh """ #!/bin/bash
    output_path=${config.outputPath}
    mkdir -p \$output_path
    repo_name=$(basename $(pwd))
    version=\$(date +'%y.%m.%d')
    report_name=\$BUILD_ID-/$repo_name-\$version.txt
    result=\$(trivy fs . -o $output_path/\$report_name )

    echo \$result
    ls -altr \$output_path
    """
}
