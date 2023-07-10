import sys 
from collections import defaultdict 
import json

filepath = sys.argv[1]

def log(message):
    print(message)
    sys.stdout.flush()

def check_vulnerabilities(data):
    critical = data["CRITICAL"] or 0
    high = data["HIGH"] or 0
    medium = data["MEDIUM"] or 0

    if critical > 0 or high > 0 or medium > 0:
        log("Critical Vulnerabilities: ")
        log(critical)

        log("High Vulnerabilities")
        log(high)
        
        log("Medium Vulnerabilities")
        log(medium)

        log("Failing pipeline ...")
        exit(1)
    pass

def summary():
        ret_json = defaultdict(int)
        try:
            with open(filepath) as f:
                result = json.load(f)
                for target in result:
                    target_info = target.get("Target")
                    if target_info.find("(") != -1:
                        ret_json["OS"] = target_info.split("(")[1].split(")")[0]
                        if target.get("Vulnerabilities"):
                            for vuln in target.get("Vulnerabilities"):
                                ret_json["TOTAL"] += 1
                                ret_json[vuln.get("Severity")] += 1
            log(ret_json)
            json.dumps(ret_json)
            check_vulnerabilities(ret_json)
            return ret_json
        except Exception as err:
            print(err)
            return {}

def main():
    summary()

if __name__ == "__main__":
    main()
