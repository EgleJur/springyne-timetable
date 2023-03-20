package lt.techin.springyne.program;

public class ProgramMapper {

    public static Program toProgram(ProgramDto programDto) {

        Program program = new Program();
        program.setName(programDto.getName());
        program.setDescription(programDto.getDescription());
        program.setDeleted(programDto.isDeleted());

        return program;
    }

    public static ProgramDto toProgramDto (Program program) {

        ProgramDto programDto = new ProgramDto();
        programDto.setName(program.getName());
        programDto.setDescription(program.getDescription());
        programDto.setDeleted(program.isDeleted());

        return programDto;
    }
}
