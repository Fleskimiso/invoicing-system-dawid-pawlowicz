package pl.futurecollars.invoicing.service

import spock.lang.Specification

class IdServiceUnitTest extends Specification {

    def nextIdPath = File.createTempFile("nextids", ".txt").toPath()
    def fileService = new FileService()

    def "should return 1 if starts from empty file"() {
        given:
        IdService idService = new IdService(nextIdPath)

        expect:
        '1' == fileService.readLinesFromFile(nextIdPath)[0]
        1 == idService.getNextIdAndSaveIncremented()
    }

    def "should increment and save correctly"() {
        given:
        fileService.writeToFile(nextIdPath, "5")
        IdService idService = new IdService(nextIdPath)

        expect:
        5 == idService.nextIdAndSaveIncremented
        ["6"] == fileService.readLinesFromFile(nextIdPath)
    }
}
