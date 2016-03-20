package grailsonetoone

class Face {
	static hasOne = [nose:Nose]
    static constraints = {
    	nose unique: true
    	nose nullable: true
    }
}
