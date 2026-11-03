const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";
const CHARLIE_UID = "charlie_789";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read user profile", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user: can create their own profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice Diski",
      email: "alice@example.com",
      favoriteTeamId: "kaizer_chiefs",
      wins: 0,
      losses: 0,
      createdAt: new Date(),
      updatedAt: new Date()
    })
  );
});

test("Authenticated user: cannot write to another user's profile", async () => {
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Attacker",
      favoriteTeamId: "orlando_pirates",
      wins: 100,
      losses: 0,
      createdAt: new Date(),
      updatedAt: new Date()
    })
  );
});

test("Authenticated user: can create a match room as host", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("match_rooms").doc("room_123").set({
      roomId: "room_123",
      roomCode: "DK-101",
      hostUserId: ALICE_UID,
      hostDisplayName: "Alice",
      hostTeamId: "kaizer_chiefs",
      status: "WAITING_FOR_PLAYER",
      currentTurn: "HOST_KICKING",
      hostScore: 0,
      guestScore: 0,
      round: 1,
      hostKicks: [],
      guestKicks: [],
      createdAt: new Date(),
      updatedAt: new Date()
    })
  );
});

test("Guest player: can join waiting match room", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("match_rooms").doc("room_join").set({
      roomId: "room_join",
      roomCode: "DK-202",
      hostUserId: ALICE_UID,
      hostDisplayName: "Alice",
      hostTeamId: "kaizer_chiefs",
      status: "WAITING_FOR_PLAYER",
      currentTurn: "HOST_KICKING",
      hostScore: 0,
      guestScore: 0,
      round: 1,
      hostKicks: [],
      guestKicks: [],
      createdAt: new Date(),
      updatedAt: new Date()
    });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertSucceeds(
    bobDb.collection("match_rooms").doc("room_join").update({
      guestUserId: BOB_UID,
      guestDisplayName: "Bob",
      guestTeamId: "orlando_pirates",
      status: "IN_PROGRESS",
      lastAction: "Bob joined the match!",
      updatedAt: new Date()
    })
  );
});

test("Non-participant: cannot modify match room", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("match_rooms").doc("room_123").set({
      roomId: "room_123",
      roomCode: "DK-101",
      hostUserId: ALICE_UID,
      hostDisplayName: "Alice",
      hostTeamId: "kaizer_chiefs",
      guestUserId: BOB_UID,
      guestDisplayName: "Bob",
      guestTeamId: "orlando_pirates",
      status: "IN_PROGRESS",
      currentTurn: "HOST_KICKING",
      hostScore: 0,
      guestScore: 0,
      round: 1,
      hostKicks: [],
      guestKicks: [],
      createdAt: new Date(),
      updatedAt: new Date()
    });
  });

  const charlieDb = testEnv.authenticatedContext(CHARLIE_UID).firestore();
  await assertFails(
    charlieDb.collection("match_rooms").doc("room_123").update({
      hostScore: 99
    })
  );
});
