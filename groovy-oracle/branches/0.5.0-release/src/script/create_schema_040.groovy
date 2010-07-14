@Grab(group='de.awtools', module='grooocle', version='0.4.0')

import de.awtools.grooocle.*
import de.awtools.grooocle.inout.*
import de.awtools.grooocle.meta.*

class Timer {
    long startTime
    long endTime

    def start() {
        startTime = System.currentTimeMillis()
    }

    def stop() {
        endTime = System.currentTimeMillis()
    }

    def out() {
        "Runtime measurement: (ms) ${endTime - startTime}"
    }
}

def timer = new Timer()
timer.start()

def sql = OraUtils.createSql('deploy', 'deploy', 'zora1entw.zit.commerzbank.com', 1521, 'RE09')
def schema = OraUtils.createOracleSchema(sql);
timer.stop()

println schema.tables

println timer.out()
