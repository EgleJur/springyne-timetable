import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import {
  TextField,
  FormControl,
  Select,
  MenuItem,
  InputLabel,
  OutlinedInput,
} from "@mui/material";

function EditGroupPage() {
  const [group, setGroup] = useState({});
  const [nameError, setNameError] = useState(false);
  const [groupYearError, setGroupYearError] = useState(false);
  const [studentsError, setStudentsError] = useState(false);
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [programs, setPrograms] = useState([]);
  const [shifts, setShifts] = useState([]);
  const [selectedProgram, setSelectedProgram] = useState("");
  const [selectedShift, setSelectedShift] = useState("");
  const params = useParams();

  const fetchGroup = () => {
    fetch("/api/v1/groups/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setGroup(jsonResponse));
  };

  useEffect(() => fetchGroup, []);

  useEffect(() => {
    fetch("api/v1/programs/")
      .then((response) => response.json())
      .then(setPrograms);
  }, []);

  useEffect(() => {
    fetch("api/v1/shifts/")
      .then((response) => response.json())
      .then(setShifts);
  }, []);

  const editGroup = (e) => {
    e.preventDefault();
    setNameError(false);
    setGroupYearError(false);
    setStudentsError(false);
    if (group.name === "" || group.groupYear === "" || group.students === "") {
      if (group.name === "") {
        setNameError(true);
      }
      if (group.groupYear === "") {
        setGroupYearError(true);
      }
      if (group.students === "") {
        setStudentsError(true);
      }
    } else {
      fetch(
        `api/v1/groups/edit/${params.id}?programId=${selectedProgram}&shiftId=${selectedShift}`,
        {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(group),
        }
      ).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
        } else {
          setFailure(true);
          setSuccess(false);
        }
      });
      //   .then(fetchGroup)
    }
  };
  const updateProperty = (property, event) => {
    setGroup({
      ...group,
      [property]: event.target.value,
    });
  };

  const handleDelete = () => {
    fetch(`/api/v1/groups/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setGroup(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
  };
  const handleRestore = () => {
    fetch(`/api/v1/groups/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setGroup(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
  };

  return (
    <div className="mx-3">
      <h2 className="mt-5">Redaguoti grupę</h2>
      {group.deleted ? <p className="mb-2">Grupė ištrinta</p> : ""}
      <p className="mb-5">Paskutinį kartą redaguota: {group.modifiedDate}</p>
      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai atnaujintas
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
          Įrašo nepavyko atnaujinti
        </Alert>
      </Collapse>

      <form noValidate>
        <TextField
          error={!!nameError}
          onChange={(e) => updateProperty("name", e)}
          value={group.name}
          id="group-name-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas negali būti tuščias laukas"
          className="form-control mb-3"
          size="small"
          disabled={group.deleted}
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          error={!!groupYearError}
          onChange={(e) => updateProperty("groupYear", e)}
          value={group.groupYear}
          id="create-group-group-year"
          label="Metai"
          helperText="Metai negali būti tuščias laukas"
          className="form-control mb-3"
          size="small"
          disabled={group.deleted}
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          error={!!studentsError}
          onChange={(e) => updateProperty("students", e)}
          value={group.students}
          id="create-group-students"
          label="Studentai"
          helperText="Studentai negali būti tuščias laukas"
          className="form-control mb-2"
          size="small"
          disabled={group.deleted}
          InputLabelProps={{ shrink: true }}
          required
        />

        <FormControl fullWidth size="small" className="mb-3">
          <InputLabel id="select-program-label" shrink>
            Programa
          </InputLabel>
          <Select
            disabled={group.deleted}
            labelId="select-program-label"
            id="select-program"
            displayEmpty
            input={<OutlinedInput notched label="Programa" />}
            fullWidth
            value={selectedProgram}
            onChange={(e) => setSelectedProgram(e.target.value)}
          >
            <MenuItem value={""} disabled>{group.program?.name}</MenuItem>
            {programs?.map((program) => (
              <MenuItem value={program.id} key={program.id}>
                {program.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <FormControl fullWidth size="small" className="mb-3">
          <InputLabel id="select-shift-label" shrink>
            Pamaina
          </InputLabel>
          <Select
            disabled={group.deleted}
            labelId="select-shift-label"
            id="select-shift"
            displayEmpty
            input={<OutlinedInput notched label="Pamaina" />}
            fullWidth
            value={selectedShift}
            onChange={(e) => setSelectedShift(e.target.value)}
          >
            <MenuItem value={""} disabled>{group.shift?.name}</MenuItem>
            {shifts?.map((shift) => (
              <MenuItem value={shift.id} key={shift.id}>
                {shift.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <div>
          <button
            type="submit"
            className="btn btn-primary me-2 mt-2 mb-5"
            onClick={editGroup}
            disabled={group.deleted}
          >
            Redaguoti
          </button>
          {group.deleted ? (
            <button
              className="btn btn-secondary me-2 mt-2 mb-5"
              onClick={handleRestore}
            >
              Atstatyti
            </button>
          ) : (
            <button
              className="btn btn-danger me-2 mt-2 mb-5"
              onClick={handleDelete}
            >
              Ištrinti
            </button>
          )}
        </div>
      </form>
    </div>
  );
}

export default EditGroupPage;