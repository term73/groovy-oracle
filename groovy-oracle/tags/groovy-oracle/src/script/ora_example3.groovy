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

schema.tables.each { tableName, table -> 
    
}

table.constraint.foreignKeys.each { println it.referencedTableName }
