const API_BASE = ""; // mismo origen: http://localhost:8080

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("transaccion-form");
    const formMessage = document.getElementById("form-message");
    const tablaBody = document.getElementById("tabla-transacciones");
    const tablaMessage = document.getElementById("tabla-message");
    const btnRecargar = document.getElementById("btn-recargar");

    const resumenPendiente = document.getElementById("resumen-pendiente");
    const resumenAprobado = document.getElementById("resumen-aprobado");
    const resumenRechazado = document.getElementById("resumen-rechazado");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        formMessage.textContent = "";
        formMessage.className = "form-message";

        const valorBase = parseFloat(document.getElementById("valorBase").value);
        const medioPago = document.getElementById("medioPago").value;
        const estadoPago = document.getElementById("estadoPago").value;

        if (isNaN(valorBase) || valorBase <= 0) {
            formMessage.textContent = "El valor base debe ser mayor a 0.";
            formMessage.classList.add("error");
            return;
        }

        const payload = { valorBase, medioPago, estadoPago };

        try {
            const resp = await fetch(`${API_BASE}/transaccion`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload),
            });

            if (!resp.ok) {
                throw new Error(`Error al crear la transacción (${resp.status})`);
            }

            await resp.json();
            form.reset();
            document.getElementById("medioPago").value = "";
            document.getElementById("estadoPago").value = "Pendiente";

            formMessage.textContent = "Transacción registrada correctamente.";
            formMessage.classList.add("ok");

            cargarTransacciones();
        } catch (err) {
            console.error(err);
            formMessage.textContent = "No se pudo guardar la transacción. Revisa el backend.";
            formMessage.classList.add("error");
        }
    });

    btnRecargar.addEventListener("click", () => {
        cargarTransacciones();
    });

    async function cargarTransacciones() {
        tablaMessage.textContent = "";
        tablaMessage.className = "table-message";

        try {
            const resp = await fetch(`${API_BASE}/transaccion`);

            if (!resp.ok) {
                throw new Error(`Error al obtener transacciones (${resp.status})`);
            }

            const data = await resp.json();
            renderizarTabla(data);
            actualizarResumen(data);

            if (!data || data.length === 0) {
                tablaMessage.textContent = "No hay transacciones registradas aún.";
                tablaMessage.classList.add("ok");
            }
        } catch (err) {
            console.error(err);
            tablaMessage.textContent = "Error al cargar las transacciones. Verifica que el backend esté en ejecución.";
            tablaMessage.classList.add("error");
        }
    }

    function renderizarTabla(transacciones) {
        tablaBody.innerHTML = "";

        transacciones.forEach((t) => {
            const tr = document.createElement("tr");

            const iva = t.iva != null ? t.iva.toFixed(2) : "-";
            const retencion = t.retencion != null ? t.retencion.toFixed(2) : "-";

            tr.innerHTML = `
                <td>${t.id ?? "-"}</td>
                <td>$ ${t.valorBase?.toFixed ? t.valorBase.toFixed(2) : t.valorBase}</td>
                <td>$ ${iva}</td>
                <td>$ ${retencion}</td>
                <td>${t.medioPago ?? "-"}</td>
                <td>${crearEstadoPillHTML(t.estadoPago)}</td>
                <td>
                    <button class="btn small outline" data-accion="aprobar" data-id="${t.id}">Aprobar</button>
                    <button class="btn small ghost" data-accion="rechazar" data-id="${t.id}">Rechazar</button>
                </td>
            `;

            tablaBody.appendChild(tr);
        });

        tablaBody.querySelectorAll("button[data-accion]").forEach((btn) => {
            btn.addEventListener("click", () => {
                const id = btn.getAttribute("data-id");
                const accion = btn.getAttribute("data-accion");
                cambiarEstado(id, accion);
            });
        });
    }

    function crearEstadoPillHTML(estado) {
        let clase = "estado-pendiente";
        let texto = estado || "Pendiente";

        if (estado === "Aprobado") clase = "estado-aprobado";
        if (estado === "Rechazado") clase = "estado-rechazado";

        return `<span class="estado-pill ${clase}">${texto}</span>`;
    }

    async function cambiarEstado(id, accion) {
        if (!id) return;

        const endpoint = accion === "aprobar"
            ? `${API_BASE}/transaccion/${id}/aprobar`
            : `${API_BASE}/transaccion/${id}/rechazar`;

        try {
            const resp = await fetch(endpoint, { method: "PUT" });

            if (!resp.ok) {
                throw new Error(`Error al actualizar estado (${resp.status})`);
            }

            cargarTransacciones();
        } catch (err) {
            console.error(err);
            alert("No se pudo actualizar el estado. Revisa el backend.");
        }
    }

    function actualizarResumen(transacciones) {
        let pend = 0, apro = 0, rech = 0;

        transacciones.forEach((t) => {
            if (t.estadoPago === "Aprobado") apro++;
            else if (t.estadoPago === "Rechazado") rech++;
            else pend++;
        });

        resumenPendiente.textContent = pend;
        resumenAprobado.textContent = apro;
        resumenRechazado.textContent = rech;
    }

    // Carga inicial
    cargarTransacciones();
});
