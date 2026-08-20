## [2.1.0](https://github.com/theEvilReaper/DartPoet/compare/2.0.3...2.1.0) (2026-08-15)


### ⚠ BREAKING CHANGES

* **class:** remove library as a valid class type ([#291](https://github.com/theEvilReaper/DartPoet/issues/291))

### Features

* add operator overloading support ([#285](https://github.com/theEvilReaper/DartPoet/issues/285)) ([06b3641](https://github.com/theEvilReaper/DartPoet/commit/06b36412c55183264a8620cca7a3fa1c8c8c5446))
* **parameter:** support super.fieldName parameters ([#279](https://github.com/theEvilReaper/DartPoet/issues/279)) ([c0e05c1](https://github.com/theEvilReaper/DartPoet/commit/c0e05c18f626daec05bd3314ff2750dd26e21d8e))
* **type:** add FunctionTypeName for inline Dart function types ([#281](https://github.com/theEvilReaper/DartPoet/issues/281)) ([6e81b7a](https://github.com/theEvilReaper/DartPoet/commit/6e81b7abe494c7d40097df2c54d949fdc8f0b50b))
* **typedef:** add support for documentation comments and annotations ([#289](https://github.com/theEvilReaper/DartPoet/issues/289)) ([4ad2c15](https://github.com/theEvilReaper/DartPoet/commit/4ad2c1599490261f3505715c57b97bfb14981b10))
* **type:** support bounded generic type parameters ([#284](https://github.com/theEvilReaper/DartPoet/issues/284)) ([1a492bd](https://github.com/theEvilReaper/DartPoet/commit/1a492bde0a3ca5579189171e695021df9d04582f))


### Bug Fixes

* correct typedef and parameter-checker validation bugs ([#288](https://github.com/theEvilReaper/DartPoet/issues/288)) ([70ab280](https://github.com/theEvilReaper/DartPoet/commit/70ab280c3307087b8caaf5105bbbc8e921cfcab5))
* **function:** emit round brackets when no parameters are present ([#282](https://github.com/theEvilReaper/DartPoet/issues/282)) ([c7ce916](https://github.com/theEvilReaper/DartPoet/commit/c7ce9163a1b3ab181eaa8a46385418322b49769e))
* **parameter:** add missign coVariant copy and add test for that case ([e3d739f](https://github.com/theEvilReaper/DartPoet/commit/e3d739fa1781534615109e5ac0e5bead71e92b45))
* **type:** doubled nullable marker on generic type arguments ([#283](https://github.com/theEvilReaper/DartPoet/issues/283)) ([533baf1](https://github.com/theEvilReaper/DartPoet/commit/533baf18d9858c9b2fbd08c9253a11c8ae7f4454))


### Miscellaneous Chores

* **class:** remove library as a valid class type ([#291](https://github.com/theEvilReaper/DartPoet/issues/291)) ([b6fc1d5](https://github.com/theEvilReaper/DartPoet/commit/b6fc1d502c89a5ae113a7c2da96785e65d084ed5))

## [2.3.0](https://github.com/theEvilReaper/DartPoet/compare/2.2.0...2.3.0) (2026-08-20)


### Features

* **type:** add support for record types ([#297](https://github.com/theEvilReaper/DartPoet/issues/297)) ([1f5b008](https://github.com/theEvilReaper/DartPoet/commit/1f5b0081292a4308093bc78aa884a775b373a24b))

## [2.2.0](https://github.com/theEvilReaper/DartPoet/compare/2.1.0...2.2.0) (2026-08-16)


### Features

* add external modifier support ([#292](https://github.com/theEvilReaper/DartPoet/issues/292)) ([4841e1f](https://github.com/theEvilReaper/DartPoet/commit/4841e1fc30d3fd5ff53ce16b02747b0aae36ff4c))
* **class:** add support for the mixin 'on' clause ([#295](https://github.com/theEvilReaper/DartPoet/issues/295)) ([29fbc69](https://github.com/theEvilReaper/DartPoet/commit/29fbc69ffb620602202a3d719bce78933072bf4e))
* **file:** add support for top level functions and properties ([#296](https://github.com/theEvilReaper/DartPoet/issues/296)) ([12a84a8](https://github.com/theEvilReaper/DartPoet/commit/12a84a81d663335f475376e82534a16c883abacb))

## [2.0.3](https://github.com/theEvilReaper/DartPoet/compare/2.0.2...2.0.3) (2026-08-07)


### Bug Fixes

* **class:** validate library classes before generation ([#274](https://github.com/theEvilReaper/DartPoet/issues/274)) ([18328fc](https://github.com/theEvilReaper/DartPoet/commit/18328fc150d5854cc2e6c67fb230687f2a5e483d))
* **deps:** update junit-framework monorepo to v6.1.3 ([#277](https://github.com/theEvilReaper/DartPoet/issues/277)) ([0d4f272](https://github.com/theEvilReaper/DartPoet/commit/0d4f2725b7e510639d8fa97c5c0a88c026fecf55))
* **typedef:** stop emitting default parameter values in typedefs ([#275](https://github.com/theEvilReaper/DartPoet/issues/275)) ([1efec65](https://github.com/theEvilReaper/DartPoet/commit/1efec655988e9ef2455d6e791e0a565d27a9e2bb))

## [2.0.2](https://github.com/theEvilReaper/DartPoet/compare/2.0.1...2.0.2) (2026-08-03)


### Bug Fixes

* **release:** Rename OneLiteFeather repository secrets ([382c6fe](https://github.com/theEvilReaper/DartPoet/commit/382c6fe93d54182271c9dc97dfd6850be34b9fdf))
* **release:** We love gradle ([7fe1bc7](https://github.com/theEvilReaper/DartPoet/commit/7fe1bc7eb164b756d7450ff9c9847c40229e246a))

## [2.0.1](https://github.com/theEvilReaper/DartPoet/compare/v2.0.0...2.0.1) (2026-08-03)


### Bug Fixes

* **release:** Bump version to 2.0.0 ([6ec36ed](https://github.com/theEvilReaper/DartPoet/commit/6ec36edc89654fde8e79aff65ebaea9528f9d22f))
* **release:** release please comment ([d8f37a7](https://github.com/theEvilReaper/DartPoet/commit/d8f37a760fb3d02ea2aab268bbcbbdf87823cac5))
* **release:** Remove from workflow some release please settings ([a480e7b](https://github.com/theEvilReaper/DartPoet/commit/a480e7bd5292123e6473ac2b547b2741e4cab7a5))
* **release:** Remove release type from packages ([238b41f](https://github.com/theEvilReaper/DartPoet/commit/238b41f19450f26c8bffafe5c894552ea4f0115f))

## [2.0.0](https://github.com/theEvilReaper/DartPoet/compare/v1.1.0...v2.0.0) (2026-08-03)


### ⚠ BREAKING CHANGES

* **inheritance:** allow deeper class inheritance ([#263](https://github.com/theEvilReaper/DartPoet/issues/263))

### Features

* **analyze:** add more test cases to the dart analyze part ([#267](https://github.com/theEvilReaper/DartPoet/issues/267)) ([e4e4ac7](https://github.com/theEvilReaper/DartPoet/commit/e4e4ac7357b329d3f7fe11ed498bb39656faaef4))
* **ci:** add Dart analyzer verification pipeline ([#262](https://github.com/theEvilReaper/DartPoet/issues/262)) ([ac048de](https://github.com/theEvilReaper/DartPoet/commit/ac048ded83190ff9255905b4daaf67a0191ca662))
* **ci:** switch to Release Please ([#257](https://github.com/theEvilReaper/DartPoet/issues/257)) ([35274e0](https://github.com/theEvilReaper/DartPoet/commit/35274e0f0991042ce29bbefe846fe7199ff8eba8))
* **class:** add support for sealed/base/interface modifiers ([#261](https://github.com/theEvilReaper/DartPoet/issues/261)) ([ac8dbd2](https://github.com/theEvilReaper/DartPoet/commit/ac8dbd22141de7985b909f7a8e018cb74a2e5fd1))


### Bug Fixes

* **class:** add JvmName annotation to avoid reserved keyword conflict ([b4a16cb](https://github.com/theEvilReaper/DartPoet/commit/b4a16cb0eb8606d1042c04b2672315b90787e9cf))
* **class:** add missing genericCast copy ([430170d](https://github.com/theEvilReaper/DartPoet/commit/430170d4fc9e2a07a4e055d0ef0662b5be1b878a))
* **class:** reject parameterized class generic declarations ([#268](https://github.com/theEvilReaper/DartPoet/issues/268)) ([18c2428](https://github.com/theEvilReaper/DartPoet/commit/18c242897260fb717fc4e563b5350a9367dea35f))


### Code Refactoring

* **inheritance:** allow deeper class inheritance ([#263](https://github.com/theEvilReaper/DartPoet/issues/263)) ([97853b2](https://github.com/theEvilReaper/DartPoet/commit/97853b28c4f96ed04c0b52929df1c4f0348eb0d0))

## [1.0.15](https://github.com/theEvilReaper/DartPoet/compare/v1.0.14...v1.0.15) (2026-07-27)


### Bug Fixes

* **code:** remove java doc syntax ([10f5c6b](https://github.com/theEvilReaper/DartPoet/commit/10f5c6b2dda7eaabe36a51bac2ddb0e7cd691ca4))

## [1.0.14](https://github.com/theEvilReaper/DartPoet/compare/v1.0.13...v1.0.14) (2026-07-20)


### Bug Fixes

* improve private modifier handling ([#254](https://github.com/theEvilReaper/DartPoet/issues/254)) ([27f6029](https://github.com/theEvilReaper/DartPoet/commit/27f6029829fe98be3ab045295b41749440b69dc1))

## [1.0.13](https://github.com/theEvilReaper/DartPoet/compare/v1.0.12...v1.0.13) (2026-07-14)


### Bug Fixes

* **deps:** update junit-framework monorepo to v6.1.2 ([#250](https://github.com/theEvilReaper/DartPoet/issues/250)) ([5b27716](https://github.com/theEvilReaper/DartPoet/commit/5b27716f6e8df4983843c522eb5b9ed27869781b))

## [1.0.12](https://github.com/theEvilReaper/DartPoet/compare/v1.0.11...v1.0.12) (2026-06-29)


### Bug Fixes

* **deps:** update junit-framework monorepo to v6.1.1 ([#246](https://github.com/theEvilReaper/DartPoet/issues/246)) ([eb32084](https://github.com/theEvilReaper/DartPoet/commit/eb32084422e28fddadc3e141f8c794ac57c08ca3))

## [1.0.11](https://github.com/theEvilReaper/DartPoet/compare/v1.0.10...v1.0.11) (2026-05-22)


### Bug Fixes

* **deps:** update junit-framework monorepo to v6.1.0 ([#239](https://github.com/theEvilReaper/DartPoet/issues/239)) ([8933739](https://github.com/theEvilReaper/DartPoet/commit/89337395dad7645e32c323bebb3c1bc22c3bf161))

## [1.0.10](https://github.com/theEvilReaper/DartPoet/compare/v1.0.9...v1.0.10) (2026-03-26)


### Bug Fixes

* string literal write ([#231](https://github.com/theEvilReaper/DartPoet/issues/231)) ([8ab647e](https://github.com/theEvilReaper/DartPoet/commit/8ab647e46fdf6879b647b54bc2495b2a42778d84))

## [1.0.9](https://github.com/theEvilReaper/DartPoet/compare/v1.0.8...v1.0.9) (2026-03-03)


### Bug Fixes

* **build:** use project version for Maven coordinates ([2484f59](https://github.com/theEvilReaper/DartPoet/commit/2484f593b93ec47ac5ff37765f08ac91dcb0da9e))

## [1.0.8](https://github.com/theEvilReaper/DartPoet/compare/v1.0.7...v1.0.8) (2026-03-03)


### Bug Fixes

* **build:** move group and version definitions to gradle.properties and adjust Maven publishing configuration ([8f67f70](https://github.com/theEvilReaper/DartPoet/commit/8f67f7095599b6a1b06e8fbbf8c3dfb19487833b))

## [1.0.7](https://github.com/theEvilReaper/DartPoet/compare/v1.0.6...v1.0.7) (2026-03-03)


### Bug Fixes

* **ci:** Retrigger build ([af466e6](https://github.com/theEvilReaper/DartPoet/commit/af466e64c7ce6fc0fdd446a626a724a1b03da28e))

## [1.0.6](https://github.com/theEvilReaper/DartPoet/compare/v1.0.5...v1.0.6) (2026-03-03)


### Bug Fixes

* **build:** update Gradle publish command in release configuration ([0eca4e9](https://github.com/theEvilReaper/DartPoet/commit/0eca4e9ef8ae491097913639c0e407f824760a6f))

## [1.0.5](https://github.com/theEvilReaper/DartPoet/compare/v1.0.4...v1.0.5) (2026-03-03)


### Bug Fixes

* **build:** add custom Maven publishing configuration ([4b608e3](https://github.com/theEvilReaper/DartPoet/commit/4b608e38d087557054b6d9da6e93f1281fc7d91b))

## [1.0.4](https://github.com/theEvilReaper/DartPoet/compare/v1.0.3...v1.0.4) (2026-03-02)


### Bug Fixes

* **deps:** update dependency org.jetbrains:annotations to v26.1.0 ([#222](https://github.com/theEvilReaper/DartPoet/issues/222)) ([ad2436b](https://github.com/theEvilReaper/DartPoet/commit/ad2436b358ba64fb95433e34b15ac2d44ff0fdb2))

## [1.0.3](https://github.com/theEvilReaper/DartPoet/compare/v1.0.2...v1.0.3) (2026-02-16)


### Bug Fixes

* **deps:** update junit-framework monorepo to v6.0.3 ([#221](https://github.com/theEvilReaper/DartPoet/issues/221)) ([a4d5f28](https://github.com/theEvilReaper/DartPoet/commit/a4d5f28dbc67c011801db4bb28a3c0bb42b43b6b))

## [1.0.2](https://github.com/theEvilReaper/DartPoet/compare/v1.0.1...v1.0.2) (2026-02-12)


### Bug Fixes

* **maven:** Improve publishing ([f593091](https://github.com/theEvilReaper/DartPoet/commit/f5930915a4a37d3320453cd28c29537e524e6779))

## [1.0.1](https://github.com/theEvilReaper/DartPoet/compare/v1.0.0...v1.0.1) (2026-02-12)


### Bug Fixes

* **maven:** Update Maven coordinates for DartPoet ([b973236](https://github.com/theEvilReaper/DartPoet/commit/b97323625908374aa7235dce2f3aa05be5c88de1))

# 1.0.0 (2026-02-08)


### Bug Fixes

* **deps:** update dependency org.junit.jupiter:junit-jupiter-engine to v6.0.2 ([#206](https://github.com/theEvilReaper/DartPoet/issues/206)) ([0de868f](https://github.com/theEvilReaper/DartPoet/commit/0de868fe0c621fffd1ee3b1fa37d2be487ee92a3))
* **deps:** update junit-framework monorepo to v6.0.1 ([#191](https://github.com/theEvilReaper/DartPoet/issues/191)) ([5eb6ab3](https://github.com/theEvilReaper/DartPoet/commit/5eb6ab304040bbebb19840dd25bd1f7c56ae736d))


### Features

* add support for %N placeholders ([#208](https://github.com/theEvilReaper/DartPoet/issues/208)) ([59ef22d](https://github.com/theEvilReaper/DartPoet/commit/59ef22d71d26152067f82f91e89ab77e45ec0855))
* generalize parameter handling to avoid duplicated code ([#212](https://github.com/theEvilReaper/DartPoet/issues/212)) ([d2ba892](https://github.com/theEvilReaper/DartPoet/commit/d2ba89236d550ac7414af3272fe03d45e6cbb9ed))
* improve typedef structure and the writing of it ([#209](https://github.com/theEvilReaper/DartPoet/issues/209)) ([77722ce](https://github.com/theEvilReaper/DartPoet/commit/77722ce931ad5e0517361cb7f4d0d43f8e7e86fc))

# Changelog

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security
