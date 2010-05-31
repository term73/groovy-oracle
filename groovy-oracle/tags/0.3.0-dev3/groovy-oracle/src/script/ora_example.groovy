import de.gluehloch.groovy.oracle.*
import de.gluehloch.groovy.oracle.meta.*

OraUtils.dataSource = null
def sql = OraUtils.createSql('test', 'test', '192.168.0.5', 1521, 'orcl')
def factory = new OracleMetaDataFactory()
def schema = factory.createOracleSchema(sql)

def tableNames = schema.tables.keySet().findAll { it.startsWith('XXX')}
tableNames.each { println it }
