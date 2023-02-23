import { useState, useEffect } from "react";
import { Alert, Collapse } from "@mui/material";
import { TextField } from "@mui/material";
import { useParams } from "react-router-dom";


function CreateGroupPage() {
  const [groupYear, setYear] = useState("");
  const [name, setName] = useState("");
  const [students, setStudents] = useState("");
  const [nameError, setNameError] = useState("");
  const [yearError, setYearError] = useState("");
  const [studentsError, setStudentsError] = useState("");
  const [programError, setProgramError] = useState("");
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
     
      if (name === "" || students===""||groupYear==="") {
        if (name === ""){setNameError(true);}
        if (students===""){setStudentsError(true);}
        if(groupYear===""){setYearError(true);}
 
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

        <label htmlFor="page-size-select" className="mb-3">
          Programa:
        </label>

        <select
          value={selectedProgram}
          // defaultValue={"default"}
          onChange={(e) => setSelectedProgram(e.target.value)}
          className={`form-control mb-3 ${selectedProgram ? "" : "border-danger"}`}
          required>
          <option value="" disabled>Pasirinkite programą</option>
          {
            programs.map(
              (prog) =>
              (<option key={prog.id}
                value={prog.id}
                disabled={prog.deleted}>{prog.name}</option>)
            )
          }
        </select>
        {!selectedProgram && (
          <div className="form-text text-danger">
            Prašome pasirinkti programą iš sąrašo.
          </div>
        )}

        <label htmlFor="page-size-select" className="mb-3">
          Pamaina:
        </label>

        <select
          value={selectedShift}
          onChange={(e) => setSelectedShift(e.target.value)}
          className={`form-control mb-3 ${selectedShift ? "" : "border-danger"}`}
          required>
          <option value="" disabled>Pasirinkite pamainą</option>
          {
            shifts.map(
              (shift) =>
              (<option key={shift.id}
                value={shift.id}>{shift.name}</option>)
            )
          }
        </select>
        {!selectedShift && (
          <div className="form-text text-danger">
            Prašome pasirinkti pamainą iš sąrašo.
          </div>
        )}

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
