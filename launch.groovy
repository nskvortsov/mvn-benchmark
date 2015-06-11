
String[] toArray(envmap) { envmap.collect { k, v -> "$k=$v" } } 

def exec(M2_HOME, maxMem) {
    def cmd = "cmd /c $M2_HOME\\bin\\mvn validate"
    def ENV = [:] + System.getenv()
    ENV["M2_HOME"] = M2_HOME
    ENV["MAVEN_OPTS"] = "-Xmx$maxMem -XX:+HeapDumpOnOutOfMemoryError"
    def process = cmd.execute(toArray(ENV) , new File("."))
    def out = new StringBuilder()
    def err = new StringBuilder()
    process.consumeProcessOutput( out, err )
    process.waitFor()
    def mem = out.find(/Final Memory:.*/)
    if (mem != null) {
        println mem
    } else {
        println out
        println err
    }
}

def counter = 1

print "3.0.5: "
for (def i = 0; i < counter; i++)
exec("C:\\Java\\apache-maven-3.0.5", args[0])

print "3.3.3: "
for (def i = 0; i < counter; i++)
exec("C:\\Java\\apache-maven-3.3.3", args[0])

