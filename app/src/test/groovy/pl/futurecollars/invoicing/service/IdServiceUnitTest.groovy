package pl.futurecollars.invoicing.service

import spock.lang.Specification

class IdServiceUnitTest extends Specification {

    def nextIdPath = File.createTempFile("nextids", ".txt").toPath()

    def "should return 1 if starts from empty file"() {
        given:
        IdService idService = new IdService(nextIdPath)

        expect:
        '1' == FileService.readLinesFromFile(nextIdPath)[0]
        1 == idService.getNextIdAndSaveIncremented()
    }

    def "should increment and save correctly"() {
        given:
        FileService.writeToFile(nextIdPath, "5")
        IdService idService = new IdService(nextIdPath)

        expect:
        5 == idService.nextIdAndSaveIncremented
        ["6"] == FileService.readLinesFromFile(nextIdPath)
    }
}
