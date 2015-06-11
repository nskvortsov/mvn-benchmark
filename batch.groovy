def groovy = "cmd /c groovy"

for(mem in ["384m", "512m", "768m", "1024m"]) {
    for( i  in  7..12) {
        println "===================="
        println "Depth: $i, Mem: $mem"
        "$groovy generate.groovy $i".execute().waitFor()
        def proc =  "$groovy launch.groovy $mem".execute()
        println proc.text
    }
}
