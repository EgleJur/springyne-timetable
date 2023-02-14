import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { TextField } from "@mui/material";

function EditTeacherPage() {
  const [teacher, setTeacher] = useState({});
  const [numberError, setNumberError] = useState("");
  const [nameError, setNameError] = useState("");
  const [lastnameError, setLastnameError] = useState("");
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
    if (teacher.lastname === "" || teacher.name === "" || teacher.number === "") {
      if (teacher.number === "") {
        setNumberError(true);
      }
      if (teacher.name === "") {
        setNameError(true);
      }
      if (teacher.lastname === "") {
        setNameError(true);
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
          error={!!numberError}
          onChange={(e) => updateProperty("number", e)}
          value={teacher.number}
          id="create-teacher-number-with-error"
          label="Numeris"
          helperText="Numeris turi būti unikalus ir negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          error={!!nameError}
          onChange={(e) => updateProperty("name", e)}
          value={teacher.name}
          id="create-teacher-number-with-error"
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
          id="create-teacher-number-with-error"
          label="Pavardė"
          helperText="Pavardė negali būti tuščias"
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
