//import de.gluehloch.sandbox.groovy.builder.*
import de.gluehloch.sandbox.groovy.oracle.*


class DatabaseBuilder {

    def name

    private final def tables = [:]

    def tables() {
    	
    }

}

def database = new DatabaseBuilder()

schema(name:'database_name') {
    table(name: 'table_name_a') {
        column(name: 'column_a', type: 'string', size: 10)
        column(name: 'column_b', type: 'numeric', size: 9, decimalplace: 2)
    }
    table(name: 'table_name_b') {
        column(name: 'column_a', type: 'integer', size: 4)
        column(name: 'column_b', type: 'string', size: 20)
    }
}

println database.name

return 0

//def db = new DatabaseBuilder()
//db.start()