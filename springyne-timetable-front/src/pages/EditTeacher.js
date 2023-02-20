import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { TextField } from "@mui/material";
import { TextCenter } from "react-bootstrap-icons";

function EditTeacherPage() {
  const [teacher, setTeacher] = useState({});
  const [nameError, setNameError] = useState("");
  const [hoursError, setHoursError] = useState("");
  const [teamsError, setTeamsError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [shifts, setShifts] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [selectedShift, setSelectedShift] = useState("");
  const [selectedSubject, setSelectedSubject] = useState("");
  const params = useParams();

  const fetchTeacher = () => {
    fetch("/api/v1/teachers/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setTeacher(jsonResponse));
  };

  // useEffect(() => fetchTeacher, [params.id]);
  useEffect(() => fetchTeacher, []);

  useEffect(() => {
    fetch("api/v1/subjects/")
      .then((response) => response.json())
      .then(setSubjects);
  }, []);

  useEffect(() => {
    fetch("api/v1/shifts/")
      .then((response) => response.json())
      .then(setShifts);
  }, []);

  const deleteSubject = (e) => {
    fetch(`/api/v1/teachers/${params.id}/removeSubject/${e}`, {
      method: "PATCH",
    }).then(fetchTeacher);
  };

  const editTeacher = (e) => {
    e.preventDefault();
    setNameError(false);
    setTeamsError(false);
    setHoursError(false);
    if (
      teacher.name === "" ||
      teacher.teamsEmail === "" ||
      teacher.hours === ""
    ) {
      if (teacher.name === "") {
        setNameError(true);
      }
      if (teacher.teamsEmail === "") {
        setTeamsError(true);
      }
      if (teacher.hours === "") {
        setHoursError(true);
      }
    } else {
      fetch(
        `/api/v1/teachers/update/${params.id}?shiftId=${selectedShift}&subjectId=${selectedSubject}`,
        {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(teacher),
        }
      ).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
          fetchTeacher();
        } else {
          setFailure(true);
          setSuccess(false);
        }
      });
    }
  };
  const updateProperty = (property, event) => {
    setTeacher({
      ...teacher,
      [property]: event.target.value,
    });
  };

  const handleDelete = () => {
    fetch(`/api/v1/teachers/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setTeacher(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
  };
  const handleRestore = () => {
    fetch(`/api/v1/teachers/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setTeacher(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
  };

  return (
    <div className="mx-3">
      <h2 className="mt-5">Redaguoti mokytoją</h2>
      {teacher.deleted ? <p className="mb-2">Mokytojas ištrintas</p> : ""}
      <p className="mb-5">Paskutinį kartą redaguotas: {teacher.modifiedDate}</p>
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
          value={teacher.name}
          id="teacher-name-with-error"
          label="Vardas ir Pavardė"
          helperText="Vardas ir Pavardė negali būti tuščias laukas"
          className="form-control mb-3"
          size="small"
          disabled={teacher.deleted}
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          error={!!teamsError}
          onChange={(e) => updateProperty("teamsEmail", e)}
          value={teacher.teamsEmail}
          id="create-teacher-teams_mail"
          label="Teams vartotojo vardas"
          helperText="Teams vardas negali būti tuščias laukas"
          className="form-control mb-3"
          size="small"
          disabled={teacher.deleted}
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          onChange={(e) => updateProperty("email", e)}
          value={teacher.email}
          id="create-teacher-email"
          label="Kontaktinis elektroninio pašto adresas"
          className="form-control mb-3"
          size="small"
          disabled={teacher.deleted}
          InputLabelProps={{ shrink: true }}
        />
        <TextField
          onChange={(e) => updateProperty("phone", e)}
          value={teacher.phone}
          id="create-teacher-phone"
          label="Kontaktinis telefonas"
          className="form-control mb-3"
          size="small"
          disabled={teacher.deleted}
          InputLabelProps={{ shrink: true }}
        />
        <TextField
          error={!!hoursError}
          onChange={(e) => updateProperty("hours", e)}
          value={teacher.hours}
          id="create-teacher-hours"
          label="Valandų skaičius"
          helperText="Valandų skaičius negali būti tuščias laukas"
          className="form-control mb-3"
          size="small"
          disabled={teacher.deleted}
          InputLabelProps={{ shrink: true }}
          required
        />
        {teacher.subjects ? <div className="mb-2">Pašalinti Dalyką:</div> : ""}

        <div className="d-grid gap-6 d-md-block">
          {teacher.subjects?.map((subject) => (
            <button
              type="submit"
              className="btn btn-light me-2 mb-2"
              value={subject.id}
              onClick={(e) => deleteSubject(e.target.value)}
              key={subject.id}
              id={subject.id}
              disabled={teacher.deleted}
            >
              {subject.name}
            </button>
          ))}
        </div>

        <label htmlFor="add-select-subject" className="my-2">
          Pridėti Dalyką
        </label>
        <select
          value={selectedSubject}
          onChange={(e) => setSelectedSubject(e.target.value)}
          id="add-select-subject"
          className="form-control mb-3"
          disabled={teacher.deleted}
        >
          <option value="">---</option>
          {subjects.map((subject) => (
            <option
              key={subject.id}
              value={subject.id}
              disabled={subject.deleted}
            >
              {subject.name}
            </option>
          ))}
        </select>

        <label htmlFor="page-size-select" className="mb-2">
          Pamaina:
        </label>
        <select
          value={selectedShift}
          onChange={(e) => setSelectedShift(e.target.value)}
          className="form-control mb-3"
          disabled={teacher.deleted}
        >
          <option value="">{teacher.shift?.name}</option>
          {shifts.map((shift) => (
            <option key={shift.id} value={shift.id} disabled={shift.deleted}>
              {shift.name}
            </option>
          ))}
        </select>
        <div>
          <button
            type="submit"
            className="btn btn-primary me-2 mt-2 mb-5"
            onClick={editTeacher}
            disabled={teacher.deleted}
          >
            Redaguoti
          </button>
          {teacher.deleted ? (
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

export default EditTeacherPage;
