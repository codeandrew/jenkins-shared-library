import sys 
import json
import requests
import datetime
import os

"""
data

Results
- Target # Image Name
  Vulnerabilities:
  - VulnerabilityID # CVE
    PkgName # Library
    InstalledVersion # #
    Severity
    Title # Title
    Description #
    PrimaryURL # Primary Source 
"""
def log(message):
    print(message)
    sys.stdout.flush()

def store_json(data, filename):
    with open(filename, 'w') as f:
        json.dump(data, f)

def send_post_request(data, url):
    headers = {'Content-Type': 'application/json'}
    r = requests.post(url, json=data, headers=headers)
    return r.json()

def create_timestamp():
    now = datetime.datetime.now()
    timestamp = now.strftime('%Y%m%d')
    return timestamp

def check_vulnerabilities(data):
    print(data)
    critical = data["CRITICAL"] or 0
    high = data["HIGH"] or 0
    medium = data["MEDIUM"] or 0



    if critical > 0 or high > 0 or medium > 0:
        print("Critical Vulnerabilities: ")
        print(critical)

        print("High Vulnerabilities")
        print(high)
        
        print("Medium Vulnerabilities")
        print(medium)

        print("Failing pipeline ...")
        sys.exit(1)
    else:
        print('Pipeline Passed')
    pass

def count_occurrences(data):
    count = {}
    for item in data:
        if item in count:
            count[item] += 1
        else:
            count[item] = 1
    return count

def main(file):
    data = {
        "vulnerabilities": []
    }
    try:
        with open(file) as f:
            result = json.load(f)
            data['image_name'] = result['ArtifactName']
            for target in result['Results']:
                print('1')
                if target.get('Vulnerabilities'):
                    print('2')
                    for v in target['Vulnerabilities']:
                        vulns = {
                            "library": v.get('PkgName', ''),
                            "vulnerability": v.get('VulnerabilityID', ''),
                            "installed_version": v.get('InstalledVersion', ''),
                            "severity": v.get('Severity', ''),
                            "title":  v.get('Title', '') ,
                            "description": v.get('Description', ''),
                            "primary_source": v.get('PrimaryURL', ''),
                            "target": target.get('Target', '') 
                        }

                        data['vulnerabilities'].append(vulns)
        
        # Overview of the data
        total_cves = [i['library'] for i in data['vulnerabilities']]
        total_vulnerable_libraries = len(set(total_cves))
        data['total_cve'] = len(total_cves)
        data['total_vulnerable_libraries'] = total_vulnerable_libraries
        print(
            f"[+] Total CVEs: { len(total_cves) }",
            f"\n[+] Total Vulnerable Libraries: { total_vulnerable_libraries }"
        )
        # print(data['vulnerabilities'])
        
        summarize_severity = []
        for i in data['vulnerabilities']:
            # print( i.get('severity'))
            summarize_severity.append( i.get('severity') )

        occurrences = count_occurrences(summarize_severity)
        check_vulnerabilities(occurrences)

    except Exception as err:
        print("[-] Something went wrong",err)
        return {}


if __name__ == "__main__":
    file = sys.argv[1]
    main(file)
