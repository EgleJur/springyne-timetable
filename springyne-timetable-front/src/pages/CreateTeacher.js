import { useState } from "react";
import { Alert,Collapse } from "@mui/material";
import { TextField } from "@mui/material";

function CreateTeacherPage() {
  const [number, setNumber] = useState("");
  const [name, setName] = useState("");
  const [lastname, setLastname] = useState("");
  const [numberError, setNumberError] = useState("");
  const [nameError, setNameError] = useState("");
  const [lastnameError, setLastnameError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);

  const createNewTeacher = (e) => {
    e.preventDefault();
    setNumberError(false);
    setNameError(false); 
    setLastnameError(false);
    if (lastname ==="" || name === "" || number === "") {
      if (number === "") {
        setNumberError(true);
      }
      if (name === "") {
        setNameError(true);
      }
      if (lastname === "") {
        setLastnameError(true);
      }
    } else {
      fetch("/api/v1/teachers/", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          number,
          name,
          lastname,
        }),
      }).then((result) => {
        if (result.ok) {
          setNumber("");
          setName("");
          setLastname("");
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
          error={!!numberError}
          onChange={(e) => setNumber(e.target.value)}
          value={number}
          id="create-teacher-number-with-error"
          label="Numeris"
          helperText="Numeris turi būti unikalus ir negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />
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
          helperText="Pavardė negali būti tuščias"
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
