import de.gluehloch.groovy.oracle.*
import de.gluehloch.groovy.oracle.meta.*

OraUtils.dataSource = null
def sql = OraUtils.createSql('test', 'test', '192.168.0.5', 1521, 'orcl')
def factory = new OracleMetaDataFactory()
def schema = factory.createOracleSchema(sql)

def table = schema.tables.get('CPUPLBONDS')
table.constraint.foreignKeys.each { println it.referencedTableName }
