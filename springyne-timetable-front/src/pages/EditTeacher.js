import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { TextField } from "@mui/material";

function EditTeacherPage() {
  const [teacher, setTeacher] = useState({});
  const [numberError, setNumberError] = useState("");
  const [nameError, setNameError] = useState("");
  const [lastnameError, setLastnameError] = useState("");
  const [teams_mailError, setTeams_mailError] = useState("");
  const [emailError, setEmailError] = useState("");
  const [phoneError, setPhoneError] = useState("");
  const [hoursError, setHoursError] = useState("");
  const [subjectError, setSubjectError] = useState("");
  const [shiftError, setShiftError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const params = useParams();

  useEffect(() => {
    fetch("/api/v1/teachers/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setTeacher(jsonResponse));
  }, [params.id]);

  const editTeacher = (e) => {
    e.preventDefault();
    setNumberError(false);
    setNameError(false);
    setLastnameError(false);
    setTeams_mailError(false);
    setEmailError(false);
    setPhoneError(false);
    setHoursError(false);
    setSubjectError(false);
    setShiftError(false);
    if (teacher.shift === "" ||teacher.subject === "" ||teacher.hours === "" ||teacher.phone === "" ||teacher.email === "" ||teacher.teams_mail === "" ||teacher.lastname === "" || teacher.name === "" || teacher.number === "") {
      if (teacher.number === "") {
        setNumberError(true);
      }
      if (teacher.name === "") {
        setNameError(true);
      }
      if (teacher.lastname === "") {
        setLastnameError(true);
      }
      if (teacher.teams_mail === "") {
        setTeams_mailError(true);
      }
      if (teacher.email === "") {
        setEmailError(true);
      }
      if (teacher.phone === "") {
        setPhoneError(true);
      }
      if (teacher.hours === "") {
        setHoursError(true);
      }
      if (teacher.subject === "") {
        setSubjectError(true);
      }
      if (teacher.shift === "") {
        setShiftError(true);
      }
    } else {
      fetch("/api/v1/teachers/update/" + params.id, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(teacher),
      }).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
        } else {
          setFailure(true);
          setSuccess(false);
          setNumberError(true);
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
          label="Vardas"
          helperText="Vardas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          error={!!lastnameError}
          onChange={(e) => updateProperty("lastname", e)}
          value={teacher.lastname}
          id="create-teacher-lastname-with-error"
          label="Pavardė"
          helperText="Pavardes laukas negali būti tuščias"
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
          helperText="Teams Vardas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
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
          helperText="Valandų skaičiaus laukas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          error={!!subjectError}
          onChange={(e) => updateProperty("subject", e)}
          value={teacher.subject}
          id="create-teacher-subject-with-error"
          label="Dalykas"
          helperText="Dalykas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          error={!!shiftError}
          onChange={(e) => updateProperty("shift", e)}
          value={teacher.shift}
          id="create-teacher-shift-with-error"
          label="Pamaina"
          helperText="Pamainos laukas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <button type="submit" className="btn btn-primary" onClick={editTeacher}>
          Redaguoti
        </button>
      </form>
    </div>
  );
}

export default EditTeacherPage;
