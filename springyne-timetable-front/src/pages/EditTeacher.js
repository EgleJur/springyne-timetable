import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { TextField } from "@mui/material";

function EditTeacherPage() {
  const [teacher, setTeacher] = useState({});
  const [nameError, setNameError] = useState("");
  const [teams_mailError, setTeams_mailError] = useState("");
  const [emailError, setEmailError] = useState("");
  const [phoneError, setPhoneError] = useState("");
  const [hoursError, setHoursError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [shifts, setShifts] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [selectedShift, setSelectedShift] = useState('');
  const [selectedSubject, setSelectedSubject] = useState('');
  const params = useParams();

  useEffect(() => {
    fetch("/api/v1/teachers/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setTeacher(jsonResponse));
  }, [params.id]);

  useEffect(() => {
    fetch('api/v1/subjects/')
      .then(response => response.json())
      .then(setSubjects)

  }, []);
  
  useEffect(() => {
    fetch('api/v1/shifts/')
      .then(response => response.json())
      .then(setShifts)

  }, []);

  const deleteSubject  = (e) => {
    fetch(`/api/v1/teachers/${params.id}/deleteSubject/${e}`, {
      method: "PATCH",
    }).then(window.location.reload(true))
  };

  const editTeacher = (e) => {
    e.preventDefault();
    setNameError(false);
    setTeams_mailError(false);
    setEmailError(false);
    setPhoneError(false);
    setHoursError(false);
    if (teacher.name === "") {
      if (teacher.name === "") {
        setNameError(true);
      }
    } else {
      fetch(`/api/v1/teachers/edit/${params.id}?
      subjectId=${selectedSubject}&shiftId=${selectedShift}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(subjects),
      }).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
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

  return (
    <div className="mx-3">
      <h2 className="my-5">Redaguoti mokytoja</h2>
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
          id="create-teacher-name-with-error"
          label="Vardas ir Pavardė"
          helperText="Vardas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          error={!!teams_mailError}
          onChange={(e) => updateProperty("teams_mail", e)}
          value={teacher.teams_mail}
          id="create-teacher-teams_mail-with-error"
          label="Teams Vardas(email)"
          helperText="Neprivaloma"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
        />
        <TextField
          error={!!emailError}
          onChange={(e) => updateProperty("email", e)}
          value={teacher.email}
          id="create-teacher-email-with-error"
          label="Kontaktinis Email"
          helperText="Neprivaloma"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
        />
        <TextField
          error={!!phoneError}
          onChange={(e) => updateProperty("phone", e)}
          value={teacher.phone}
          id="create-teacher-phone-with-error"
          label="Kontaktinis telefonas"
          helperText="Neprivaloma"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
        />
        <TextField
          error={!!hoursError}
          onChange={(e) => updateProperty("hours", e)}
          value={teacher.hours}
          id="create-teacher-hours-with-error"
          label="Valandu skaicius"
          helperText="Neprivaloma"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <label htmlFor="page-size-select" className="mb-3">
          Dalykai:
        </label>
        <div className="d-grid gap-6 d-md-block">
          {teacher.subjects?.map((subject) => (
          <button
            type="submit"
            className="btn btn-light"
            value={subject.id}
            onClick={(e) => deleteSubject(e.target.value)}
            key={subject.id} id={subject.id}
            >{subject.name}</button>
        ))}</div>

        <select
          value={selectedSubject}
          onChange={(e) => setSelectedSubject(e.target.value)}
          className="form-control mb-3">
          <option value=''>---</option>
          {
            subjects.map(
              (subject) =>
              (<option key={subject.id} 
                  value={subject.id} 
                  disabled={subject.deleted}>{subject.name}</option>)
          )
          }
        </select>
        
        <label htmlFor="page-size-select" className="mb-3">
        Pamaina:
      </label>
      <select
            value={selectedShift}
            onChange={(e) => setSelectedShift(e.target.value)}
            className="form-control mb-3">
              <option value=''>{teacher.shift?.name}</option>
            {
                shifts.map(
                    (shift) =>
                    (<option key={shift.id} 
                        value={shift.id}
                        disabled={shift.deleted}>{shift.name}</option>)
                )
            }
        </select>
        
        <button type="submit" className="btn btn-primary" onClick={editTeacher}>
          Redaguoti
        </button>
      </form>
    </div>
  );
}

export default EditTeacherPage;
