import { useState, useEffect } from "react";
import { Alert, Collapse } from "@mui/material";
import { TextField, FormControl, MenuItem, Select, InputLabel } from "@mui/material";
import { useParams } from "react-router-dom";


function CreateGroupPage() {
  const [groupYear, setYear] = useState("");
  const [name, setName] = useState("");
  const [students, setStudents] = useState("");
  const [nameError, setNameError] = useState(false);
  const [yearError, setYearError] = useState(false);
  const [studentsError, setStudentsError] = useState(false);
  const [programError, setProgramError] = useState(false);
  const [shiftError, setShiftError] = useState(false);
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const params = useParams();
  const [programs, setPrograms] = useState([]);
  const [selectedProgram, setSelectedProgram] = useState("");
  const [shifts, setShifts] = useState([]);
  const [selectedShift, setSelectedShift] = useState("");

  useEffect(() => {
    fetch('api/v1/programs/')
      .then(response => response.json())
      .then(setPrograms)

  }, []);

  useEffect(() => {
    fetch('api/v1/shifts/')
      .then(response => response.json())
      .then(setShifts)

  }, []);


  const createNewSubject = (e) => {
    e.preventDefault();
    setNameError(false);
    setStudentsError(false);
    setYearError(false);
    setProgramError(false);
    setShiftError(false);

    if (name === "" || students === "" 
    || groupYear === "" || selectedProgram==="" 
    || selectedShift === "") {
      if (name === "") { setNameError(true); }
      if (students === "") { setStudentsError(true); }
      if (groupYear === "") { setYearError(true); }
      if (selectedProgram === "") { setProgramError(true); }
      if (selectedShift === "") { setShiftError(true); }

    } else {
      fetch(
        `/api/v1/groups/createGroup?programId=${selectedProgram}&shiftId=${selectedShift}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name,
          groupYear,
          students,

        })
        ,
      }).then((result) => {
        if (result.ok) {
          setName("");
          setYear("");
          setStudents("");
          setSuccess(true);
          setFailure(false);
        } else {
          setFailure(true);
          setSuccess(false);
        }
      });
    }
  };


  return (
    <div className="mx-3">
      <h2 className="my-5">Pridėti naują grupę</h2>
      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai sukurtas
        </Alert>
      </Collapse>

      <Collapse in={failure}>
        <Alert
          onClose={() => {
            setFailure(false);
          }}
          severity="error"
          className="mb-3"
        >
          Įrašo nepavyko sukurti
        </Alert>
      </Collapse>
      <form noValidate>

        <TextField
          error={!!nameError}
          onChange={(e) => setName(e.target.value)}
          value={name}
          id="create-group-name-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />

        <TextField
          error={!!yearError}
          onChange={(e) => setYear(e.target.value)}
          value={groupYear}
          id="create-group-year-with-error"
          label="Mokslo metai"
          helperText="Mokslo metai negali būti tušti"
          className="form-control mb-3"
          size="small"
          required
        />

        <TextField
          error={!!studentsError}
          onChange={(e) => setStudents(e.target.value)}
          value={students}
          id="create-group-students-with-error"
          label="Studentų skaičius"
          helperText="Studentų skaičius negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />

        <FormControl fullWidth size="small" className="mb-3">
          <InputLabel id="select-program-label" error={programError} required>
            Pasirinkite programą
          </InputLabel>
          <Select
            error={programError}
            labelId="select-program-label"
          //  InputLabelProps={{ shrink: true }}
            id="add-select-program"
            label="Pasirinkite programą"
            fullWidth
            value={selectedProgram}
            // defaultValue={"default"}
            onChange={(e) => setSelectedProgram(e.target.value)}
            required>
            {
              programs?.map(
                (prog) =>(
                <MenuItem
                key={prog.id}
                value={prog.id}
                disabled={prog.deleted}>
                  {prog.name}
                </MenuItem>
              ))}
          </Select>
        </FormControl>

        <FormControl fullWidth size="small" className="mb-3">
          <InputLabel id="select-shift-label" error={shiftError} required>
            Pasirinkite pamainą
          </InputLabel>
          <Select
            error={shiftError}
            labelId="select-shift-label"
            //InputLabelProps={{ shrink: true }}
            id="add-select-shift"
            label="Pasirinkite pamainą"
            fullWidth
            value={selectedShift}
            // defaultValue={"default"}
            onChange={(e) => setSelectedShift(e.target.value)}
            required>
            {
              shifts.map(
                (shift) =>(
                <MenuItem
                key={shift.id}
                value={shift.id}>{shift.name}
                </MenuItem>
              ))}
          </Select>
        </FormControl>

        <button
          type="submit"
          className="btn btn-primary"
          onClick={createNewSubject}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateGroupPage;
