import { useState } from "react";
import { useHref } from "react-router-dom";

function CreateModulePage() {
  const [number, setNumber] = useState("");
  const [name, setName] = useState("");
  const [inputClassName1, setImputClassName1] = useState("form-control");
  const [inputClassName2, setImputClassName2] = useState("form-control");

  const createNewModule = () => {
    if (name === "" || number === "") {
      if (number === "") {
        setImputClassName1("form-control border-danger-subtle");
      }
      if (name === "") {
        setImputClassName2("form-control border-danger-subtle");
      }
    } else {
      fetch("/api/v1/modules/", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          number,
          name,
        }),
      }).then((result) => {
        if (result.ok) {
          setNumber("");
          setName("");
          setImputClassName1("form-control");
          setImputClassName2("form-control");
          window.alert("Įrašas sėkmingai sukurtas");
        } else {
          setImputClassName1("form-control border-danger-subtle");
          window.alert("Įrašo nepavyko sukurti");
        }
      });
    }
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Pridėti naują modulį</h2>

      <form>
        <div className="mb-3">
          <label htmlFor="create-module-number" className="form-label">
            Numeris
          </label>
          <input
            type="text"
            className={inputClassName1}
            id="create-module-number"
            aria-describedby="module-number-help"
            value={number}
            onChange={(e) => setNumber(e.target.value)}
          />
          <div id="module-number-help" className="form-text">
            Numeris turi būti unikalus ir negali būti tuščias
          </div>
        </div>
        <div className="mb-3">
          <label htmlFor="create-module-name" className="form-label">
            Pavadinimas
          </label>
          <input
            type="text"
            className={inputClassName2}
            id="create-module-name"
            aria-describedby="module-name-help"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <div id="module-name-help" className="form-text">
            Pavadinimas negali būti tuščias
          </div>
        </div>
        <button
          type="submit"
          className="btn btn-primary"
          onClick={createNewModule}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateModulePage;
