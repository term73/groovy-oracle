//
// Show all referencing tables. (Show all tables with a foreign key to the
// requested table).
//
import de.gluehloch.groovy.oracle.*
import de.gluehloch.groovy.oracle.meta.*

OraUtils.dataSource = null
def sql = OraUtils.createSql('test', 'test', '192.168.0.5', 1521, 'orcl')
def factory = new OracleMetaDataFactory()
def schema = factory.createOracleSchema(sql)

def referencedTables = []

schema.tables.each { tableName, table -> 
    def fks = table.constraint.foreignKeys.findAll { fk -> fk.referencedTableName == 'TABLE_NAME' }
    fks.each { fk -> referencedTables << fk.name }
}

referencedTables.each { println it }
return 0