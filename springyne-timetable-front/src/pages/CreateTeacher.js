import { useState, useEffect } from "react";
import { Alert,Collapse } from "@mui/material";
import { TextField } from "@mui/material";

function CreateTeacherPage() {
  const [name, setName] = useState("");
  const [teams_mail, setTeams_mail] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [hours, setHours] = useState("");
  const [nameError, setNameError] = useState("");
  const [teams_mailError, setTeams_mailError] = useState("");
  // const [emailError, setEmailError] = useState("");
  // const [phoneError, setPhoneError] = useState("");
  const [hoursError, setHoursError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [shifts, setShifts] = useState([]);
    const [selectedShifts, setSelectedShifts] = useState('');

    const [subjects, setSubjects] = useState([]);
    const [selectedSubject, setSelectedSubject] = useState('');


    useEffect(() => {
      fetch('api/v1/shifts/')
          .then(response => response.json())
          .then(setShifts)
          
  }, []);

  useEffect(() => {
    fetch('api/v1/subjects/')
        .then(response => response.json())
        .then(setSubjects)
        
}, []);

  const createNewTeacher = (e) => {
    e.preventDefault();
    setNameError(false); 
    setTeams_mailError(false);
    // setEmailError(false);
    // setPhoneError(false);
    setEmail();
    setPhone();
    setHoursError(false);
    if (name === "") {
        setNameError(true);
      
    } else {
      fetch(`/api/v1/teachers?shiftId=${selectedShifts}&subjectId=${selectedSubject}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name,
          teams_mail,
          email,
          phone,
          hours,
        }),
      }).then((result) => {
        if (result.ok) {
          setName("");
          setTeams_mail("");
          setEmail("");
          setPhone("");
          setHours("");
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
          id="create-teacher-name-with-error"
          label="Vardas ir Pavardė"
          helperText="Vardas ir Pavardė negali būti tuščias"
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
          helperText="Neprivaloma"
          className="form-control mb-3"
          size="small"
        />
        <TextField
          // error={!!emailError}
          onChange={(e) => setEmail(e.target.value)}
          value={email}
          id="create-teacher-email-with-error"
          label="Kontaktinis email"
          helperText="Neprivaloma"
          className="form-control mb-3"
          size="small"
        />
        <TextField
          // error={!!phoneError}
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
          helperText="Neprivaloma"
          className="form-control mb-3"
          size="small"
        />
        <label htmlFor="page-size-select" className="mb-3">
          Dalykas:
        </label>
        
        <select
            value={selectedSubject}
            onChange={(e) => setSelectedSubject(e.target.value)}
            className="form-control mb-3">
              <option value =''>---</option>
            {
                subjects.map(
                    (sub) =>
                    (<option key={sub.id} 
                        value={sub.id} 
                        disabled={sub.deleted}>{sub.name}</option>)
                )
            }
        </select>

        <label htmlFor="page-size-select" className="mb-3">
  Pamaina:
</label>
<select
  value={selectedShifts}
  onChange={(e) => setSelectedShifts(e.target.value)}
  className={`form-control mb-3 ${selectedShifts ? "" : "border-danger"}`}
  required
>
  <option value="">Pasirinkite pamainą</option>
  {
    shifts.map((shift) => (
      <option
        key={shift.id}
        value={shift.id}
        disabled={shift.deleted}
      >
        {shift.name}
      </option>
    ))
  }
</select>
{!selectedShifts && (
  <div className="form-text text-danger">
    Prašome pasirinkti pamainą iš sąrašo.
  </div>
)}



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