import { useState } from "react";
import { Alert,Collapse } from "@mui/material";
import { TextField } from "@mui/material";

function CreateTeacherPage() {
  const [name, setName] = useState("");
  const [lastname, setLastname] = useState("");
  const [teams_mail, setTeams_mail] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [hours, setHours] = useState("");
  const [subject, setSubject] = useState("");
  const [shift, setShift] = useState("");
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

  const createNewTeacher = (e) => {
    e.preventDefault();
    setNameError(false); 
    setLastnameError(false);
    setTeams_mailError(false);
    setEmailError(false);
    setPhoneError(false);
    setHoursError(false);
    setSubjectError(false);
    setShiftError(false);
    if (lastname ==="" || name === "" || teams_mail === "" || email === ""|| phone === ""|| hours === ""|| subject === ""|| shift === "") {
      if (name === "") {
        setNameError(true);
      }
      if (lastname === "") {
        setLastnameError(true);
      }
      if (teams_mail === "") {
        setTeams_mailError(true);
      }
      if (email === "") {
        setEmailError(true);
      }
      if (phone === "") {
        setPhoneError(true);
      }
      if (hours === "") {
        setHoursError(true);
      }
      if (subject === "") {
        setSubjectError(true);
      }
      if (shift === "") {
        setShiftError(true);
      }
    } else {
      fetch("/api/v1/teachers/", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name,
          lastname,
          teams_mail,
          email,
          phone,
          hours,
          subject,
          shift,
        }),
      }).then((result) => {
        if (result.ok) {
          setName("");
          setLastname("");
          setTeams_mail("");
          setEmail("");
          setPhone("");
          setHours("");
          setSubject("");
          setShift("");
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
      <h2 className="my-5">Pridėti naują mokytoja</h2>
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
          id="create-teacher-number-with-error"
          label="Vardas"
          helperText="Vardas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          error={!!lastnameError}
          onChange={(e) => setLastname(e.target.value)}
          value={lastname}
          id="create-teacher-number-with-error"
          label="Pavardė"
          helperText="Pavardes laukas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          error={!!teams_mailError}
          onChange={(e) => setTeams_mail(e.target.value)}
          value={teams_mail}
          id="create-teacher-teams_mail-with-error"
          label="Teams Vardas(email)"
          helperText="Teams Vardas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          error={!!emailError}
          onChange={(e) => setEmail(e.target.value)}
          value={email}
          id="create-teacher-email-with-error"
          label="Kontaktinis email"
          helperText="Neprivaloma"
          className="form-control mb-3"
          size="small"
        />
        <TextField
          error={!!phoneError}
          onChange={(e) => setPhone(e.target.value)}
          value={phone}
          id="create-teacher-phone-with-error"
          label="Kontaktinis telefonas"
          helperText="Neprivaloma"
          className="form-control mb-3"
          size="small"
        />
        <TextField
          error={!!hoursError}
          onChange={(e) => setHours(e.target.value)}
          value={hours}
          id="create-teacher-hours-with-error"
          label="Valandų skaičius"
          helperText="Valandų skaičiaus laukas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          error={!!subjectError}
          onChange={(e) => setSubject(e.target.value)}
          value={subject}
          id="create-teacher-subject-with-error"
          label="Dalykas"
          helperText="Dalykas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          error={!!shiftError}
          onChange={(e) => setShift(e.target.value)}
          value={shift}
          id="create-teacher-shift-with-error"
          label="Pamaina"
          helperText="Pamainos laukas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />
        <button
          type="submit"
          className="btn btn-primary"
          onClick={createNewTeacher}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateTeacherPage;
