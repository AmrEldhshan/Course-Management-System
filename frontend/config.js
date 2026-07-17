// API base URLs.
// When running via Docker Compose (browser on host machine, containers on
// localhost-mapped ports), these point at the host-exposed ports.
// Change these if you deploy the services somewhere else.

const ADMIN_API_BASE = "http://localhost:8080/api/admin";
const PUBLIC_API_BASE = "http://localhost:8082/api/public";
